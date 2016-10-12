package CMSKeysManager;

import java.io.BufferedReader;
import java.io.IOException;

public class CMSKey
{
	private String keyName, keyTitle, keyDescription, keyComments;

	public void setKey(String inKeyName, String inKeyTitle, String inKeyDescription, String inKeyComments)
	{
		keyName = inKeyName;
		keyTitle = inKeyTitle;
		keyDescription = inKeyDescription;
		keyComments = inKeyComments;
	}

	public String getName()
	{
		return keyName;
	}

	public String getTitle()
	{
		return keyTitle;
	}

	public String getDescription()
	{
		return keyDescription;
	}

	public String getComments()
	{
		return keyComments;
	}

	public void keyCopy(CMSKey copyKey)
	{
		keyName = copyKey.getName();
		keyTitle = copyKey.getTitle();
		keyDescription = copyKey.getDescription();
		keyComments = copyKey.getComments();

	}

	public boolean equals(CMSKey compareKey)
	{
		boolean result = true;
		if (!keyName.equals(compareKey.getName()))
		{
			result = false;
		}
		if (!(keyTitle == null))
		{
			if (!keyTitle.equals(compareKey.getTitle()))
			{
				result = false;
			}
		}
		if (!keyDescription.equals(compareKey.getDescription()))
		{
			result = false;
		}
		if (!keyComments.equals(compareKey.getComments()))
		{
			result = false;
		}
		return result;
	}

	public boolean equals(CMSKey compareKey, String debugStr)
	{
		boolean result = true;
		if (!keyName.equals(compareKey.getName()))
		{
			System.out.println("Name issue:");
			System.out.println("src =|" + compareKey.getName() + "|");
			System.out.println("dest=|" + keyName + "|");
			result = false;
		}
		if (!(keyTitle == null))
		{
			if (!keyTitle.equals(compareKey.getTitle()))
			{
				System.out.println("Title issue:");
				System.out.println("src =|" + compareKey.getTitle() + "|");
				System.out.println("dest=|" + keyTitle + "|");
				result = false;
			}
		}
		if (!keyDescription.equals(compareKey.getDescription()))
		{
			System.out.println("Description issue:");
			System.out.println("src =|" + compareKey.getDescription() + "|");
			System.out.println("dest=|" + keyDescription + "|");
			result = false;
		}
		if (!keyComments.equals(compareKey.getComments()))
		{
			System.out.println("Comments issue:");
			System.out.println("src =|" + compareKey.getComments() + "|");
			System.out.println("dest=|" + keyComments + "|");
			result = false;
		}
		return result;
	}

	public boolean mergeDecision(String sectionName, CMSKey compareKey, BufferedReader br, String logFile)
	{
		System.out.println("MISMATCH --- " + this.keyName);
		boolean mergeChoice = false;
		// Never going to be name, because that is what we match on
		if (!keyName.equals(compareKey.getName()))
		{
			System.out.println("Name issue:");
			System.out.println("src =|" + compareKey.getName() + "|");
			System.out.println("dest=|" + keyName + "|");
		}
		if (!(keyTitle == null))
		{
			if (!keyTitle.equals(compareKey.getTitle()))
			{
				System.out.println("Title issue:");
				System.out.println("src =|" + compareKey.getTitle() + "|");
				System.out.println("dest=|" + keyTitle + "|");
			}
		}
		if (!keyDescription.equals(compareKey.getDescription()))
		{
			System.out.println("Description issue:");
			System.out.println("src =|" + compareKey.getDescription() + "|");
			System.out.println("dest=|" + keyDescription + "|");
		}
		if (!keyComments.equals(compareKey.getComments()))
		{
			System.out.println("Comments issue:");
			System.out.println("src =|" + compareKey.getComments() + "|");
			System.out.println("dest=|" + keyComments + "|");
		}

		try
		{
			System.out.print("Merge this difference? (Y / N) ");
			String input = br.readLine();
			if ("Y".equals(input) || "y".equals(input))
			{
				mergeChoice = true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return mergeChoice;
	}
}
